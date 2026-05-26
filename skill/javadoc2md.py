#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
JavaDoc HTML to Markdown converter for orion-kit.

Converts JavaDoc HTML class files into clean, searchable Markdown.
One .md file per class, organized by module and package.

Usage:
    python javadoc2md.py
    python javadoc2md.py --output-dir ~/.claude/skills/orion-kit/references
"""

import os
import re
import sys
import argparse

MODULES = [
    'orion-lang',
    'orion-ext',
    'orion-office',
    'orion-http',
    'orion-net',
    'orion-web',
    'orion-spring',
    'orion-generator',
]

SKIP_FILES = {
    'index.html', 'overview-summary.html', 'overview-frame.html',
    'overview-tree.html', 'allclasses-frame.html', 'allclasses-noframe.html',
    'constant-values.html', 'deprecated-list.html', 'help-doc.html',
    'index-all.html', 'package-list', 'serialized-form.html',
    'stylesheet.css', 'script.js',
}


def strip_html(text):
    """Remove HTML tags and decode entities, preserving generic type brackets."""
    # Decode entities first so &lt;/&gt; become literal < > (not mistaken for tags)
    text = text.replace('&lt;', '\x00LT\x00').replace('&gt;', '\x00GT\x00')
    text = text.replace('&amp;', '&')
    text = text.replace('&#39;', "'").replace('&quot;', '"')
    text = text.replace('&nbsp;', ' ').replace('\xa0', ' ')
    # Remove HTML tags
    text = re.sub(r'<[^>]+>', '', text)
    # Restore angle brackets
    text = text.replace('\x00LT\x00', '<').replace('\x00GT\x00', '>')
    # Clean up whitespace (but not inside < > generics)
    text = re.sub(r'\s+', ' ', text)
    # Remove spaces around < > in generics: "Map < String >" -> "Map<String>"
    text = re.sub(r'\s*<\s*', '<', text)
    text = re.sub(r'\s*>', '>', text)
    # Add space between > and following word char if missing: "String>name" -> "String> name"
    text = re.sub(r'>(\w)', r'> \1', text)
    # Restore comma-space in generics: "Map<String,String>" -> "Map<String, String>"
    text = re.sub(r'<(\w)', lambda m: '<' + m.group(1), text)
    text = re.sub(r',(\S)', r', \1', text)
    return text.strip()


def extract_class_info(html):
    """Extract class name, package, and description from JavaDoc HTML."""
    info = {'name': '', 'package': '', 'description': '', 'type': 'class'}

    # Package: <div class="subTitle">cn.orionsec.kit.lang.utils</div>
    pkg_m = re.search(r'<div\s+class="subTitle">(.*?)</div>', html, re.DOTALL)
    if pkg_m:
        info['package'] = strip_html(pkg_m.group(1)).strip()

    # Class title: <h2 title="Class Strings" class="title">Class Strings</h2>
    title_m = re.search(r'<h2[^>]*>(.*?)</h2>', html, re.DOTALL)
    if title_m:
        raw_title = strip_html(title_m.group(1)).strip()
        m = re.match(r'(Class|Interface|Enum|Annotation Type)\s+(\w+)', raw_title)
        if m:
            info['type'] = m.group(1).lower()
            info['name'] = m.group(2)

    if not info['name']:
        return None

    # Description: first <div class="block"> after class declaration
    # Use .*? before <hr> to handle interfaces with superinterface <dl> blocks
    desc_m = re.search(
        r'<div class="description">\s*<ul class="blockList">\s*<li class="blockList">.*?<hr>.*?<div class="block">(.*?)</div>',
        html, re.DOTALL
    )
    if desc_m:
        info['description'] = strip_html(desc_m.group(1)).strip()

    return info


def extract_fields(html):
    """Extract field summary."""
    fields = []
    # Match FIELD SUMMARY section: anchor -> table -> end of </table>
    # Handles classes with no METHOD SUMMARY or METHOD DETAIL (e.g. constant interfaces)
    section = re.search(
        r'<a\s+name="field\.summary".*?<table[^>]*class="memberSummary"[^>]*>(.*?)</table>',
        html, re.DOTALL | re.IGNORECASE
    )
    if not section:
        return fields

    rows = re.findall(r'<tr\s+class="(?:altColor|rowColor)"[^>]*>(.*?)</tr>', section.group(1), re.DOTALL)
    for row in rows:
        # <td class="colFirst"><code>static String</code></td>
        # <td class="colLast"><code><span class="memberNameLink"><a ...>EMPTY</a></span></code>&nbsp;</td>
        type_m = re.search(r'<td\s+class="colFirst"><code>(.*?)</code></td>', row, re.DOTALL)
        name_m = re.search(r'<span\s+class="memberNameLink"><a[^>]*>(\w+)</a></span>', row)
        desc_m = re.search(r'<div\s+class="block">(.*?)</div>', row, re.DOTALL)

        if name_m:
            ftype = strip_html(type_m.group(1)).strip() if type_m else ''
            fname = name_m.group(1)
            fdesc = strip_html(desc_m.group(1)).strip() if desc_m else ''
            fields.append({'type': ftype, 'name': fname, 'description': fdesc})

    return fields


def extract_methods(html):
    """Extract methods from method summary and method detail sections."""
    methods = []

    # Method summary section
    summary_section = re.search(r'METHOD SUMMARY(.*?)METHOD DETAIL', html, re.DOTALL | re.IGNORECASE)
    if not summary_section:
        return methods

    rows = re.findall(r'<tr\s+id="i\d+"[^>]*>(.*?)</tr>', summary_section.group(1), re.DOTALL)

    for row in rows:
        method = parse_method_row(row)
        if method:
            methods.append(method)

    # Enrich with method detail (parameter descriptions, return descriptions)
    detail_section = re.search(r'METHOD DETAIL(.*)', html, re.DOTALL | re.IGNORECASE)
    if detail_section:
        enrich_method_details(detail_section.group(1), methods)

    return methods


def parse_method_row(row):
    """Parse a single method summary row."""
    # Return type
    ret_m = re.search(r'<td\s+class="colFirst"><code>(.*?)</code></td>', row, re.DOTALL)
    return_type = strip_html(ret_m.group(1)).strip() if ret_m else ''

    # Method name and params - match to )</code> to handle generics with nested parens
    name_m = re.search(
        r'<span\s+class="memberNameLink"><a[^>]*>(\w+)</a></span>\((.*?)(?=\)</code>)',
        row, re.DOTALL
    )
    if not name_m:
        return None

    method_name = name_m.group(1)
    raw_params = name_m.group(2)
    params = parse_params(raw_params)

    # Description
    desc_m = re.search(r'<div\s+class="block">(.*?)</div>', row, re.DOTALL)
    desc = strip_html(desc_m.group(1)).strip() if desc_m else ''

    return {
        'name': method_name,
        'return_type': return_type,
        'params': params,
        'description': desc,
        'return_desc': '',
    }


def parse_params(raw):
    """Parse method parameters from HTML."""
    params = []
    # Strip HTML first so &lt;/&gt; become real < > for proper generic handling
    clean = strip_html(raw).strip()
    if not clean:
        return params

    # Split by comma, respecting angle brackets (now real < >)
    parts = []
    depth = 0
    current = ''
    for ch in clean:
        if ch == '<':
            depth += 1
            current += ch
        elif ch == '>':
            depth -= 1
            current += ch
        elif ch == ',' and depth == 0:
            parts.append(current)
            current = ''
        else:
            current += ch
    if current.strip():
        parts.append(current)

    for part in parts:
        part = part.strip()
        if not part:
            continue
        # "String s" -> type="String", name="s"
        tokens = part.rsplit(None, 1)
        if len(tokens) == 2:
            params.append({'type': tokens[0], 'name': tokens[1], 'description': ''})
        elif len(tokens) == 1:
            params.append({'type': tokens[0], 'name': '', 'description': ''})

    return params


def enrich_method_details(detail_html, methods):
    """Enrich methods with parameter and return descriptions from detail section."""
    # Split by method anchors: <a name="methodName-type1-type2-">
    blocks = re.split(r'<a\s+name="', detail_html)

    for block in blocks[1:]:
        # Get anchor name
        anchor_m = re.match(r'([^"]+)"', block)
        if not anchor_m:
            continue
        anchor = anchor_m.group(1)

        # Find matching method by name
        # Anchor format: "methodName-type1-type2-" or "methodName-"
        # Extract method name (before first '-')
        dash_idx = anchor.find('-')
        method_name = anchor[:dash_idx] if dash_idx > 0 else anchor
        # Handle overloaded methods: match by name + param count
        matched = None
        for m in methods:
            if m['name'] == method_name:
                matched = m
                break

        if not matched:
            continue

        # Extract parameter descriptions
        # <dt><span class="paramLabel">Parameters:</span></dt>
        # <dd><code>name</code> - desc</dd>
        param_section = re.search(r'Parameters:</span></dt>(.*?)(?:</dl>|Returns:|Throws:)', block, re.DOTALL)
        if param_section:
            param_descs = re.findall(
                r'<dd>\s*<code>(\w+)</code>\s*-\s*(.*?)\s*</dd>',
                param_section.group(1), re.DOTALL
            )
            for pname, pdesc in param_descs:
                pdesc = strip_html(pdesc).strip()
                for p in matched['params']:
                    if p['name'] == pname:
                        p['description'] = pdesc

        # Extract return description
        return_section = re.search(r'Returns:</span></dt>\s*<dd>(.*?)</dd>', block, re.DOTALL)
        if return_section:
            matched['return_desc'] = strip_html(return_section.group(1)).strip()


def to_markdown(info, fields, methods):
    """Convert parsed data to Markdown string."""
    lines = []
    lines.append(f'# {info["name"]}')
    lines.append('')
    if info['package']:
        lines.append(f'**Package:** `{info["package"]}`')
        lines.append('')
    if info['description']:
        lines.append(info['description'])
        lines.append('')

    # Fields
    if fields:
        lines.append('## Fields')
        lines.append('')
        for f in fields:
            if f['description']:
                lines.append(f'- `{f["type"]} {f["name"]}` - {f["description"]}')
            else:
                lines.append(f'- `{f["type"]} {f["name"]}`')
        lines.append('')

    # Methods
    if methods:
        lines.append('## Methods')
        lines.append('')
        for method in methods:
            # Build signature
            sig_parts = []
            if method['return_type']:
                sig_parts.append(method['return_type'])
            sig_parts.append(method['name'])

            param_strs = []
            for p in method['params']:
                ps = p['type']
                if p['name']:
                    ps += ' ' + p['name']
                param_strs.append(ps)

            sig = f"{' '.join(sig_parts)}({', '.join(param_strs)})"
            lines.append(f'### `{sig}`')
            lines.append('')

            if method.get('description'):
                lines.append(method['description'])
                lines.append('')

            # Parameter details
            has_desc = any(p.get('description') for p in method['params'])
            if has_desc:
                lines.append('**Parameters:**')
                for p in method['params']:
                    if p.get('description'):
                        lines.append(f'- `{p["name"]}` ({p["type"]}): {p["description"]}')
                    elif p['name']:
                        lines.append(f'- `{p["name"]}` ({p["type"]})')
                lines.append('')

            if method.get('return_desc'):
                lines.append(f'**Returns:** {method["return_desc"]}')
                lines.append('')

    return '\n'.join(lines)


def find_class_html_files(javadoc_dir):
    """Find all class HTML files in a JavaDoc directory."""
    class_files = []
    for root, dirs, files in os.walk(javadoc_dir):
        for f in files:
            if f.endswith('.html') and f not in SKIP_FILES:
                filepath = os.path.join(root, f)
                rel = os.path.relpath(filepath, javadoc_dir)
                if 'class-use' in rel or 'package-summary' in rel or 'package-tree' in rel:
                    continue
                # Skip package-level files
                if f.startswith('package-'):
                    continue
                class_files.append(filepath)
    return class_files


def convert_module(module_name, project_dir, output_base):
    """Convert all class docs for a module to Markdown."""
    javadoc_dir = os.path.join(project_dir, module_name, 'target', 'reports', 'apidocs')
    if not os.path.isdir(javadoc_dir):
        print(f'  {module_name}/ (SKIPPED - no JavaDoc found)')
        return 0

    output_dir = os.path.join(output_base, module_name)
    os.makedirs(output_dir, exist_ok=True)

    class_files = find_class_html_files(javadoc_dir)
    converted = 0

    for filepath in class_files:
        try:
            with open(filepath, 'r', encoding='utf-8', errors='replace') as f:
                html = f.read()

            info = extract_class_info(html)
            if info is None:
                continue

            fields = extract_fields(html)
            methods = extract_methods(html)
            md = to_markdown(info, fields, methods)

            # Output path mirrors package structure
            rel = os.path.relpath(filepath, javadoc_dir)
            md_rel = rel.replace('.html', '.md')
            md_path = os.path.join(output_dir, md_rel)

            os.makedirs(os.path.dirname(md_path), exist_ok=True)
            with open(md_path, 'w', encoding='utf-8') as f:
                f.write(md)

            converted += 1
        except Exception as e:
            print(f'  ERROR: {os.path.basename(filepath)}: {e}')

    return converted


def main():
    parser = argparse.ArgumentParser(description='Convert JavaDoc HTML to Markdown')
    parser.add_argument('--javadoc-dir', default=None,
                        help='Project root directory (default: script directory)')
    parser.add_argument('--output-dir', default=None,
                        help='Output directory for Markdown (default: <project>/skill/references)')
    args = parser.parse_args()

    script_dir = os.path.dirname(os.path.abspath(__file__))
    project_dir = args.javadoc_dir or script_dir
    default_output = os.path.join(os.path.expanduser('~'), '.claude', 'skills', 'orion-kit', 'references')
    output_base = args.output_dir or default_output

    print('============================================')
    print('  JavaDoc HTML -> Markdown Converter')
    print('============================================')
    print()
    print(f'Project dir: {project_dir}')
    print(f'Markdown output: {output_base}')
    print()

    total = 0
    for module in MODULES:
        count = convert_module(module, project_dir, output_base)
        print(f'  {module}: {count} classes')
        total += count

    print()
    print(f'Total: {total} classes converted')
    print('============================================')


if __name__ == '__main__':
    main()
